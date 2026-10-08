# BlocMon – règles de travail

Mêmes règles que le dépôt : spec (`docs/01-spec.md`) → plan (`docs/02-plan.md`) → code → tests → revue.
- Toute évolution commence par la spec, avec des critères d'acceptation testables.
- `game/` : Kotlin pur, sans dépendance Android, tests unitaires. Le hasard passe par l'interface `Dice` pour rester testable.
- `ui/` : affiche un `GameState`, aucune règle de jeu.
- Les créatures sont originales (pas de personnages de marque).
