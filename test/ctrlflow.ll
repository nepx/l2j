; ModuleID = 'ctrlflow.c'
source_filename = "ctrlflow.c"
target datalayout = "e-m:e-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

; Function Attrs: norecurse nounwind readnone uwtable
define dso_local i32 @main(i32, i8** nocapture readnone) local_unnamed_addr #0 {
  %3 = icmp slt i32 %0, 2
  br i1 %3, label %25, label %4

4:                                                ; preds = %2
  %5 = add nsw i32 %0, -1
  %6 = sub nsw i32 1, %0
  %7 = mul i32 %5, %6
  %8 = zext i32 %5 to i33
  %9 = add nsw i32 %0, -2
  %10 = zext i32 %9 to i33
  %11 = mul i33 %8, %10
  %12 = lshr i33 %11, 1
  %13 = trunc i33 %12 to i32
  %14 = add i32 %7, %13
  br label %15

15:                                               ; preds = %15, %4
  %16 = phi i32 [ %17, %15 ], [ %14, %4 ]
  %17 = sub i32 %16, %0
  %18 = icmp sgt i32 %17, -1
  br i1 %18, label %15, label %19

19:                                               ; preds = %15
  %20 = add nsw i32 %0, 5
  br label %21

21:                                               ; preds = %19, %21
  %22 = phi i32 [ %23, %21 ], [ %17, %19 ]
  %23 = add nsw i32 %20, %22
  %24 = icmp slt i32 %23, 100
  br i1 %24, label %21, label %25

25:                                               ; preds = %21, %2
  %26 = phi i32 [ 0, %2 ], [ %23, %21 ]
  ret i32 %26
}

attributes #0 = { norecurse nounwind readnone uwtable "correctly-rounded-divide-sqrt-fp-math"="false" "disable-tail-calls"="false" "less-precise-fpmad"="false" "min-legal-vector-width"="0" "no-frame-pointer-elim"="false" "no-infs-fp-math"="false" "no-jump-tables"="false" "no-nans-fp-math"="false" "no-signed-zeros-fp-math"="false" "no-trapping-math"="false" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "unsafe-fp-math"="false" "use-soft-float"="false" }

!llvm.module.flags = !{!0}
!llvm.ident = !{!1}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{!"clang version 9.0.1-+20191211110317+c1a0a213378-1~exp1~20191211221711.104 "}
