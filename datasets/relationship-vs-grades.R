mat<-read.csv("student-mat.csv")
por<-read.csv("student-por.csv")

boxplot(mat$G3[mat$romantic == "no"], mat$G3[mat$romantic == "yes"], por$G3[por$romantic == "no"], por$G3[por$romantic == "yes"], las=2, col=c("royalblue2", "palevioletred1", "royalblue2", "palevioletred1"), at=c(1,2, 4,5), names=c("Maths (single)", "Maths (in relationship)", "Por (single)", "Por (in relationship)"), ylab="Final Grade (0 - 20)")
