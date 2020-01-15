package com.ashtray.carracinggame2d

class LazySingleton  private constructor() {

    @JvmField
    var position: Int=0

    private object HOLDER {
        val INSTANCE = LazySingleton()
    }

    companion object {
        val INSTANCE: LazySingleton by lazy { HOLDER.INSTANCE }
    }


}