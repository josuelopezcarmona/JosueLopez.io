(() => {
  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)');

  /* ---------- Mobile navigation ---------- */
  const nav = document.querySelector('.site-nav');
  const toggle = nav.querySelector('.nav-toggle');
  const menu = document.getElementById('nav-menu');

  const setMenu = (open) => {
    nav.classList.toggle('is-open', open);
    toggle.setAttribute('aria-expanded', String(open));
    toggle.setAttribute('aria-label', open ? 'Close menu' : 'Open menu');
  };

  toggle.addEventListener('click', () => setMenu(!nav.classList.contains('is-open')));
  menu.addEventListener('click', (e) => {
    if (e.target.closest('a')) setMenu(false);
  });
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && nav.classList.contains('is-open')) {
      setMenu(false);
      toggle.focus();
    }
  });
  document.addEventListener('click', (e) => {
    if (nav.classList.contains('is-open') && !nav.contains(e.target)) setMenu(false);
  });

  /* ---------- Active section highlight ---------- */
  const links = [...menu.querySelectorAll('.nav-links a[href^="#"]')];
  const linkFor = new Map(links.map((a) => [a.getAttribute('href').slice(1), a]));
  const spy = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (!entry.isIntersecting) return;
      links.forEach((a) => a.removeAttribute('aria-current'));
      linkFor.get(entry.target.id)?.setAttribute('aria-current', 'true');
    });
  }, { rootMargin: '-45% 0px -50% 0px' });
  document.querySelectorAll('main > section[id]').forEach((s) => spy.observe(s));

  /* ---------- Reveal on scroll ---------- */
  const reveals = document.querySelectorAll('.reveal');
  if (reduceMotion.matches) {
    reveals.forEach((el) => el.classList.add('is-visible'));
  } else {
    const revealer = new IntersectionObserver((entries, obs) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return;
        entry.target.classList.add('is-visible');
        obs.unobserve(entry.target);
      });
    }, { rootMargin: '0px 0px -8% 0px', threshold: 0.08 });
    reveals.forEach((el) => revealer.observe(el));
  }

  /* ---------- Experience filter + alternating timeline ---------- */
  const timeline = document.querySelector('.timeline');
  const items = [...timeline.querySelectorAll('.tl-item')];
  const filterBtns = document.querySelectorAll('.filter-btn');
  const filterStatus = document.getElementById('filter-status');

  const layoutTimeline = () => {
    let i = 0;
    items.forEach((item) => {
      if (item.hidden) return;
      item.classList.toggle('is-left', i % 2 === 0);
      item.classList.toggle('is-right', i % 2 === 1);
      i += 1;
    });
    return i;
  };

  filterBtns.forEach((btn) => {
    btn.addEventListener('click', () => {
      const filter = btn.dataset.filter;
      filterBtns.forEach((b) => b.setAttribute('aria-pressed', String(b === btn)));
      items.forEach((item) => {
        item.hidden = filter !== 'all' && item.dataset.type !== filter;
        if (!item.hidden) item.classList.add('is-visible');
      });
      const count = layoutTimeline();
      filterStatus.textContent = `Showing ${count} ${count === 1 ? 'entry' : 'entries'}`;
    });
  });

  timeline.classList.add('is-alt');
  layoutTimeline();

  /* ---------- Hero parallax, nav elevation, offscreen pause ---------- */
  const hero = document.querySelector('.hero');
  const layers = [...hero.querySelectorAll('[data-parallax]')];
  let heroVisible = true;
  let ticking = false;

  new IntersectionObserver(([entry]) => {
    heroVisible = entry.isIntersecting;
    hero.classList.toggle('is-paused', !heroVisible);
  }).observe(hero);

  const onScroll = () => {
    ticking = false;
    const y = window.scrollY;
    nav.classList.toggle('is-scrolled', y > 24);
    if (!heroVisible || reduceMotion.matches) return;
    layers.forEach((layer) => {
      layer.style.transform = `translate3d(0, ${(y * parseFloat(layer.dataset.parallax)).toFixed(1)}px, 0)`;
    });
  };

  window.addEventListener('scroll', () => {
    if (ticking) return;
    ticking = true;
    requestAnimationFrame(onScroll);
  }, { passive: true });
  onScroll();

  /* ---------- Footer year ---------- */
  document.getElementById('year').textContent = String(new Date().getFullYear());
})();
