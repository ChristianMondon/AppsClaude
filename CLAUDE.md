# Morpion – règles de travail

Cycle AI-native SDLC : 1) `docs/01-spec.md` (quoi) → 2) `docs/02-plan.md` (comment, tâches) → 3) code → 4) tests → 5) revue.
- Toute évolution commence par une mise à jour de la spec, avec critères d'acceptation testables.
- La logique de jeu vit dans `game/` : Kotlin pur, sans dépendance Android, couverte par des tests unitaires.
- L'UI (`ui/`) ne contient aucune règle de jeu ; elle affiche un `GameState`.
- Petits commits, un par tâche du plan.
