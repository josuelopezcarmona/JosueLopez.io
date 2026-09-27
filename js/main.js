(() => {
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

  const links = [...menu.querySelectorAll('.nav-links a')];
  const linkFor = new Map(links.map((a) => [a.getAttribute('href').slice(1), a]));
  const setActive = (id) => {
    links.forEach((a) => a.removeAttribute('aria-current'));
    linkFor.get(id)?.setAttribute('aria-current', 'true');
  };
  const atBottom = () => window.innerHeight + window.scrollY >= document.documentElement.scrollHeight - 2;

  const spy = new IntersectionObserver((entries) => {
    if (atBottom()) return;
    entries.forEach((entry) => {
      if (entry.isIntersecting) setActive(entry.target.id);
    });
  }, { rootMargin: '-40% 0px -55% 0px' });
  document.querySelectorAll('main > section[id]').forEach((s) => spy.observe(s));

  // The last section can't scroll into the observer band, so mark it when the page bottoms out.
  window.addEventListener('scroll', () => {
    if (atBottom()) setActive(links[links.length - 1].getAttribute('href').slice(1));
  }, { passive: true });

  document.getElementById('year').textContent = String(new Date().getFullYear());
})();
