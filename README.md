# Getting Started
### Git

#### Branches

- **`main`** - production source code.
- **`development`** - staging source code.

#### Pull Request flow

```
<ticket-number>: <ticket-title>
```

##### Example:

`TBQ-23: Update readme.md`

#### Branch flow

```
<type>/<ticket-number>-<short-desc>
```

##### Types:

- task
- fix

##### Examples:

- `task/TBQ-23-update-readme`
- `fix/TBQ-XX-fix-some-bug`

#### Commit flow

```
<ticket-number>: <modifier> <desc>
```

##### Modifiers:

- `+` (add)
- `*` (edit)
- `-` (remove)

##### Examples:

- `TBQ-xx: + add something`
- `TBQ-xx: * change something`
- `TBQ-xx: - remove something`
