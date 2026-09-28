# JosueLopez.io
Personal Website

A static, dependency-free portfolio site (HTML, CSS, vanilla JavaScript) with a simple, minimal design.
Open `index.html` directly in a browser, or serve the folder with any static host (e.g. GitHub Pages).

## Structure

```
index.html                  Main page (all sections)
css/styles.css              Design tokens, components, sections, responsive rules
js/main.js                  Mobile nav menu and active-section highlight
assets/img/                 Profile photo
assets/favicon.svg          Site icon
assets/resume/              Downloadable resume PDF
resume/index.html           Print-ready source for the resume PDF
code/index.html             Read-only code viewer for the projects below
code/manifest.js            List of files shown in the viewer (add new files here)
code/coffeemaker/           CoffeeMaker backend (Spring Boot) and frontend (React/Vite) source
code/wolfdash/              WolfDash (RideShareManager) source, tests, and input files
code/wolfregrade/           WolfRegrade source files
```

The code viewer loads files with `fetch`, so it needs to be served over HTTP (GitHub Pages works); it won't load files when `code/index.html` is opened directly from disk.

## Updating the resume PDF

Edit `resume/index.html`, then regenerate the PDF with headless Edge (or Chrome):

```bash
msedge --headless=new --no-pdf-header-footer --print-to-pdf="assets/resume/Josue-Lopez-Carmona-Resume.pdf" "file:///<absolute-path>/resume/index.html"
```

Or replace `assets/resume/Josue-Lopez-Carmona-Resume.pdf` with an exported copy of your original resume (keep the filename).
