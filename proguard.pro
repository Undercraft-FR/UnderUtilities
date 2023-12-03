libraryjars <java.home>/lib/rt.jar

-dontoptimize
-dontpreverify
-dontwarn **
-keepattributes *Annotation*

-keep public class fr.skhr.loyto.underutilities.** {
   public protected <methods>;
}

-keepclassmembers !public class fr.skhr.loyto.underutilities.** {
   public protected <methods>;
}

-keep public class net.minecraft.** {
*;
}

-keepclassmembers enum  * {
   public static **[] values();
   public static ** valueOf(java.lang.String);
}