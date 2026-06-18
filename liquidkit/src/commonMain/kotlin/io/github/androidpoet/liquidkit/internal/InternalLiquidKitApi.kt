package io.github.androidpoet.liquidkit.internal

/**
 * Marks declarations that are public only to allow cross-module access within
 * the LiquidKit library itself.  Do not use from application code — binary
 * compatibility for these symbols is not guaranteed.
 */
@RequiresOptIn(
    message = "This is an internal LiquidKit API. It may change or be removed without notice.",
    level = RequiresOptIn.Level.ERROR,
)
@Retention(AnnotationRetention.BINARY)
public annotation class InternalLiquidKitApi
