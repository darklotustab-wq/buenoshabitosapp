# HealthTracker v1.1.0

App Android nativa de seguimiento de salud (comidas con fotos, hábitos, agua).

## Novedades v1.1.0

- **Visor de fotos**: tocá una foto ya sacada → se abre en pantalla completa con pinch-to-zoom, doble-tap y arrastre.
- **Historial funcional**: calendario mensual; tocá un día para ver sus fotos y hábitos cumplidos. Puntito verde = día con actividad.
- **Progreso real**: % de cumplimiento últimos 30 días, gráfico de barras de la semana, rachas por hábito (🔥 actual + mejor), % por hábito.
- **Ajustes completos**:
  - Objetivo de vasos de agua (1–20)
  - Editar/borrar hábitos (los base no se borran, sólo se editan)
  - Recordatorios con horario configurable
  - Borrar todos los datos

## Migración

La base de datos sube a versión 2 con migración 1→2 (agrega tabla `settings`).
Tus datos previos (comidas, hábitos, agua, completions) se preservan.

## Build

`git push` → GitHub Actions compila `app-debug.apk` automáticamente.
Descargalo desde la pestaña Actions del repo.
