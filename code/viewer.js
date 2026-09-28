(() => {
  const projects = window.CODE_PROJECTS;
  const sidebar = document.getElementById('viewer-files');
  const nameEl = document.getElementById('file-name');
  const pathEl = document.getElementById('file-path');
  const authorEl = document.getElementById('file-author');
  const rawLink = document.getElementById('raw-link');
  const codeEl = document.getElementById('code');
  const links = new Map();

  const dirOf = (path) => path.slice(0, path.lastIndexOf('/'));
  const baseName = (path) => path.slice(path.lastIndexOf('/') + 1);

  projects.forEach((project) => {
    const section = document.createElement('section');
    section.className = 'viewer-project';
    const title = document.createElement('h2');
    title.textContent = project.name;
    const sub = document.createElement('p');
    sub.textContent = project.subtitle;
    section.append(title, sub);

    let list;
    let currentDir = null;
    project.files.forEach((file) => {
      if (dirOf(file) !== currentDir) {
        currentDir = dirOf(file);
        const dir = document.createElement('h3');
        dir.textContent = currentDir;
        list = document.createElement('ul');
        section.append(dir, list);
      }
      const key = `${project.id}/${file}`;
      const a = document.createElement('a');
      a.href = `#${key}`;
      a.textContent = baseName(file);
      const li = document.createElement('li');
      li.append(a);
      list.append(li);
      links.set(key, a);
    });
    sidebar.append(section);
  });

  const resolve = (hash) => {
    const key = decodeURIComponent(hash.replace(/^#/, ''));
    if (links.has(key)) return key;
    const project = projects.find((p) => p.id === key) || projects[0];
    return `${project.id}/${project.files[0]}`;
  };

  const render = (text) => {
    const frag = document.createDocumentFragment();
    text.replace(/\r\n?/g, '\n').replace(/\n$/, '').split('\n').forEach((line) => {
      const span = document.createElement('span');
      span.className = 'line';
      span.textContent = line + '\n';
      frag.append(span);
    });
    codeEl.replaceChildren(frag);
  };

  const load = async () => {
    const key = resolve(location.hash);
    links.forEach((a, k) => (k === key ? a.setAttribute('aria-current', 'page') : a.removeAttribute('aria-current')));
    links.get(key).scrollIntoView({ block: 'nearest' });

    nameEl.textContent = baseName(key);
    pathEl.textContent = key;
    authorEl.textContent = '';
    rawLink.href = key;
    rawLink.hidden = false;
    document.title = `${baseName(key)} | Project Code | Josue Lopez-Carmona`;

    try {
      const res = await fetch(key);
      if (!res.ok) throw new Error(res.statusText);
      const text = await res.text();
      const authors = [...new Set([...text.matchAll(/@author\s+(.+)/g)].map((m) => m[1].trim()))];
      if (authors.length) authorEl.textContent = `${authors.length > 1 ? 'Authors' : 'Author'}: ${authors.join(', ')}`;
      render(text);
    } catch {
      codeEl.textContent = 'This file could not be loaded. If you opened the site directly from your computer, view it through a web server (such as GitHub Pages) instead.';
    }
  };

  window.addEventListener('hashchange', load);
  load();
})();
