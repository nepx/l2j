; ModuleID = 'arrtest.c'
source_filename = "arrtest.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@msg = dso_local constant [15 x i8] c"Hello, world!\0A\00", align 1

; Function Attrs: nofree nounwind uwtable
define dso_local i32 @main() local_unnamed_addr #0 {
  %1 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 0))
  %2 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 1))
  %3 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 2))
  %4 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 3))
  %5 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 4))
  %6 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 5))
  %7 = tail call i32 @puts(i8* getelementptr inbounds ([15 x i8], [15 x i8]* @msg, i64 0, i64 6))
  ret i32 0
}

; Function Attrs: nofree nounwind
declare dso_local i32 @puts(i8* nocapture readonly) local_unnamed_addr #1

attributes #0 = { nofree nounwind uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }
attributes #1 = { nofree nounwind "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.1-+20191211110317+c1a0a213378-1~exp1~20191211221711.104 "}
