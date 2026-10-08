# Chat & Souris – règles de travail

Même méthode que le dépôt : besoin → PRD → spécification → plan → code → tests → revue (documents dans `docs/`, tous validés).
- La spécification (`docs/02-specification-technique.md`) et le plan (`docs/03-plan.md`) font foi ; tout écart se discute avant le code.
- `game/` : Kotlin pur, sans Android ni Compose, testé. `step()` est pure et déterministe.
- `render/` et `ui/` : aucune règle de jeu.
- Valeurs d'équilibrage uniquement dans `Params.kt` et `LevelDef.kt`.
- Une tâche du plan = un petit commit ; une PR par jalon.
