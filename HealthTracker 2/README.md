# HealthTracker — App Android de seguimiento de alimentación y rutina saludable

App nativa Android (Kotlin + Jetpack Compose) con base de datos local (Room).

## Funcionalidades implementadas

### Dashboard
- Header con día de la semana, fecha y barra de progreso del día.
- Sección **Alimentación**: 4 tarjetas (desayuno, almuerzo, merienda, cena).
  - Tocar una tarjeta abre la **cámara** para sacar foto del plato.
  - La foto se guarda localmente y aparece en la tarjeta con check verde.
- Tracker de **agua** (vasos / objetivo) con +/-.
- Sección **Hábitos del día** con checklist colorido:
  - 5 hábitos base (medicación, actividad física, instrumento, algo positivo, meditación).
  - Posibilidad de **agregar hábitos personalizados** con ícono y color a elección.
  - Tocar un hábito alterna hecho/pendiente.
- Barra de navegación inferior (Hoy / Historial / Progreso / Ajustes).

### Persistencia
- Room database con 4 entidades: `Meal`, `Habit`, `HabitCompletion`, `WaterIntake`.
- Todo se guarda por fecha (`yyyy-MM-dd`) → preparado para historial.
- Hábitos base se siembran automáticamente la primera vez.

## Cómo abrir y correr en Android Studio

1. **Descomprimir** el ZIP.
2. Abrir **Android Studio Hedgehog (2023.1.1)** o superior.
3. `File → Open` y seleccionar la carpeta `HealthTracker`.
4. Esperar a que Gradle sincronice (puede tardar la primera vez, baja dependencias).
5. Conectar un teléfono Android (USB con depuración) o crear un emulador (API 24+).
6. Apretar el botón **Run** (Shift+F10).

> La primera ejecución pedirá permiso de **cámara** al tocar una comida.

## Próximos pasos sugeridos

- Pantalla de **Historial**: ver días pasados con sus fotos y hábitos.
- Pantalla de **Progreso**: gráficos de racha por hábito (semana, mes).
- **Notificaciones** locales para recordar medicación / actividad.
- Editar título, ícono y color de hábitos existentes.
- Exportar fotos + datos a CSV.
- Backup en Google Drive.

## Estructura

```
app/src/main/java/com/healthtracker/app/
├── HealthTrackerApp.kt          ← Application class
├── MainActivity.kt
├── data/
│   ├── local/                   ← Room: DAOs, Database, Converters
│   ├── model/                   ← Entidades (Meal, Habit, etc.)
│   └── repository/              ← HealthRepository
├── ui/
│   ├── components/              ← DayHeader, MealCard, HabitRow, WaterTracker...
│   ├── dashboard/               ← DashboardScreen
│   └── theme/
├── util/                        ← DateUtils, IconMap, PhotoUtils
└── viewmodel/                   ← DashboardViewModel
```
