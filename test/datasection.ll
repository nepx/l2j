; ModuleID = 'datasection.c'
source_filename = "datasection.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [25 x i8] c"You passed no arguments\0A\00", align 1
@.str.1 = private unnamed_addr constant [25 x i8] c"You passed one argument\0A\00", align 1
@.str.2 = private unnamed_addr constant [26 x i8] c"You passed two arguments\0A\00", align 1
@.str.3 = private unnamed_addr constant [28 x i8] c"You passed three arguments\0A\00", align 1
@.str.4 = private unnamed_addr constant [27 x i8] c"You passed four arguments\0A\00", align 1
@.str.5 = private unnamed_addr constant [27 x i8] c"You passed five arguments\0A\00", align 1
@.str.6 = private unnamed_addr constant [26 x i8] c"You passed six arguments\0A\00", align 1
@messages = dso_local local_unnamed_addr global [8 x i8*] [i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str, i32 0, i32 0), i8* getelementptr inbounds ([25 x i8], [25 x i8]* @.str.1, i32 0, i32 0), i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.2, i32 0, i32 0), i8* getelementptr inbounds ([28 x i8], [28 x i8]* @.str.3, i32 0, i32 0), i8* getelementptr inbounds ([27 x i8], [27 x i8]* @.str.4, i32 0, i32 0), i8* getelementptr inbounds ([27 x i8], [27 x i8]* @.str.5, i32 0, i32 0), i8* getelementptr inbounds ([26 x i8], [26 x i8]* @.str.6, i32 0, i32 0), i8* null], align 16

; Function Attrs: nofree nounwind uwtable
define dso_local i32 @main(i32, i8** nocapture readnone) local_unnamed_addr #0 {
  %3 = icmp sgt i32 %0, 6
  br i1 %3, label %9, label %4

4:                                                ; preds = %2
  %5 = sext i32 %0 to i64
  %6 = getelementptr inbounds [8 x i8*], [8 x i8*]* @messages, i64 0, i64 %5
  %7 = load i8*, i8** %6, align 8, !tbaa !2
  %8 = tail call i32 @puts(i8* %7)
  br label %9

9:                                                ; preds = %2, %4
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
!2 = !{!3, !3, i64 0}
!3 = !{!"any pointer", !4, i64 0}
!4 = !{!"omnipotent char", !5, i64 0}
!5 = !{!"Simple C/C++ TBAA"}
