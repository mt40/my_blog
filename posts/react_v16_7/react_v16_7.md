# React v16.7: No, This Is Not The One With Hooks

Our latest release includes an important performance bugfix for `React.lazy`. Although there are no API changes, we’re releasing it as a minor instead of a patch.

## Installation

React v16.7.0 is available on the npm registry.

To install React 16 with Yarn, run:
```bash
yarn add react@^16.7.0 react-dom@^16.7.0
```

To install React 16 with npm, run:
```bash
npm install --save react@^16.7.0 react-dom@^16.7.0
```

We also provide UMD builds of React via a CDN:

```html
<script crossorigin src="https://unpkg.com/react@16/umd/react.production.min.js"></script>
<script crossorigin src="https://unpkg.com/react-dom@16/umd/react-dom.production.min.js"></script>
```
---

## Changelog
### React DOM
- Fix performance of React.lazy for large numbers of lazily-loaded components. (@acdlite in #14429)
- Clear fields on unmount to avoid memory leaks. (@trueadm in #14276)
- Fix bug with SSR and context when mixing react-dom/server@16.6 and react@<16.6. (@gaearon in #14291)
- Fix a performance regression in profiling mode. (@bvaughn in #14383)

### Scheduler (Experimental)
- Post to MessageChannel instead of window. (@acdlite in #14234)
- Reduce serialization overhead. (@developit in #14249)
- Fix fallback to setTimeout in testing environments. (@bvaughn in #14358)
- Add methods for debugging. (@mrkev in #14053)

