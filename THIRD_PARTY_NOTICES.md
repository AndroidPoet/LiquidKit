# Third-Party Notices

## AndroidLiquidGlass

LiquidKit vendors Android Liquid Glass renderer and component source from:

https://github.com/Kyant0/AndroidLiquidGlass

Original author: Kyant  
License: Apache License 2.0

The copied files live under:

```text
liquidkit/src/androidMain/kotlin/com/kyant/backdrop
liquidkit/src/androidMain/kotlin/com/kyant/backdrop/catalog/components
liquidkit/src/androidMain/kotlin/com/kyant/backdrop/catalog/utils
sampleApp/src/androidMain/kotlin/com/kyant/backdrop/catalog
sampleApp/src/androidMain/res
```

LiquidKit does not depend on the published AndroidLiquidGlass artifact. The
renderer/component source is copied into the Android source set so LiquidKit can
expose one stable multiplatform API while rendering Android with custom Liquid
Glass and iOS with native Apple Liquid Glass/UIKit components.

## Navigation 3 Recipes

LiquidKit's sample app adapts the multiple-back-stack Navigation 3 sample shape
from:

https://github.com/terrakok/nav3-recipes

License: Apache License 2.0

The adapted sample code lives in:

```text
sampleApp/src/commonMain/kotlin/io/github/androidpoet/liquidkit/sample/LiquidKitSampleApp.kt
```

LiquidKit does not expose Navigation 3 from the library module. The dependency
is scoped to the sample app to demonstrate how `LiquidBottomNavigation` can be
used as controlled UI while Navigation 3 owns the app's back stacks.
