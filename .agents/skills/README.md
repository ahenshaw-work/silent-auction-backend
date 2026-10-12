# Project skills

Add a skill here when a workflow is repeated and is not obvious from [`AGENTS.md`](../../AGENTS.md).

Follow the [Agent Skills](https://agentskills.io/specification) layout:

```
.agents/skills/<skill-name>/SKILL.md
```

`SKILL.md` starts with YAML frontmatter:

```yaml
---
name: skill-name
description: What the skill does and when to use it.
---
```

- `name` matches the directory name: lowercase letters, numbers, and hyphens, at most 64 characters.
- `description` is third person, says what the skill does and when to use it, and is at most 1024 characters.
- Keep `SKILL.md` under 500 lines. Put long reference in `references/`, executables in `scripts/`, and templates in `assets/`.
- Link those files with relative paths one level down from `SKILL.md`.
- Do not create skills under `.cursor/skills/`.
