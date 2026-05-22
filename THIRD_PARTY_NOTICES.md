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
