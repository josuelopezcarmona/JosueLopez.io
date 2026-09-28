# Coffee Maker Frontend

Uses React + Vite.

## Running

1. Install the libraries: `npm install`
2. Start the application: `npm run dev`
3. Browse to http://localhost:3000/

The frontend calls the `coffee_maker` backend, so start the backend first or
the pages will load with no data.

## Other commands

| Command | Description |
|---|---|
| `npm run lint` | Check the source for problems with ESLint |
| `npm run build` | Build a production bundle into `dist/` |
| `npm run preview` | Serve the production bundle locally |

## Backend URL

The backend URL defaults to `http://localhost:8080` (see
`src/services/ApiConfig.js`). To point at a different backend, create a
`.env.local` file in this directory:

```
VITE_API_BASE_URL=http://localhost:9090
```
