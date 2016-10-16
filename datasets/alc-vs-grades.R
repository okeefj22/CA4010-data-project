mat<-read.csv('student-mat.csv')
por<-read.csv('student-por.csv')

boxplot(mat$G3[(mat$Walc + mat$Dalc) <= median(mat$Walc + mat$Dalc)], mat$G3[(mat$Walc + mat$Dalc) > median(mat$Walc + mat$Dalc)], por$G3[(por$Walc + por$Dalc) <= median(por$Walc + por$Dalc)], por$G3[(por$Walc + por$Dalc) > median(por$Walc + por$Dalc)], las=2, col=c("royalblue2", "palevioletred1", "royalblue2", "palevioletred1"), at=c(1,2, 4,5), names=c("Maths (low alc)", "Maths (high alc)", "Por (low alc)", "Por (high alc)"), ylab="Final Grade (0 - 20)")
