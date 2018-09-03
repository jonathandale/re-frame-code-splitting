# re-frame code-split example

A simple `re-frame` template, with code-splitting, compiled with shadow-cljs
.

### Install Javascript dependencies
```bash
yarn
```

### Run shadow-cljs dev server
```bash
npx shadow-cljs watch app

open http://localhost:8280
```

### Build app
```bash
npx shadow-cljs release app
```

### Serve app
Serve `public` dir as an SPA. I recommend [serve](https://www.npmjs.com/package/serve) for serving a dir for an SPA.

```bash
cd public
serve -s -n -o
```
