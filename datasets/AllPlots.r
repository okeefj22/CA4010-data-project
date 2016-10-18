por <- read.csv(file="comma-student-por.csv", header=TRUE)
mat <- read.csv(file="comma-student-mat.csv", header=TRUE)


#Scatter plot health vs absences 
#plot(values$health, values$absences, ylab = "absences", xlab="health", main = "Health vs Absences", col = "red")

#scatter plot health vs Results
#plot(values$health, values$G3, ylab = "Results", xlab="health", main = "Health vs Results", col = "red")

unhealthyPor <- por[por$health == "1",]
healthyPor <- por[por$health == "5",]
unhealthyMat <- mat[mat$health == "1",]
healthyMat <- mat[mat$health == "5",]


#boxplot(healthy$absences, main = "absences of the most healty people (5/5)")

#boxplot(unhealthy$absences, main = "absences of the most un-healthy people (1/5)")

#boxplot(healthy$G3, main = "results of the most healty people (5/5)")

#boxplot(unhealthy$G3, main = "results of the most un-healthy people (1/5)")

#some standard deviations
#x <- sd(values$G3)
#z <- sd(healthy$G3)
#y <- sd(unhealthy$G3)
#print("Standard deviation of students results:") 
#print(x)
#print("Standard deviation of healthiest students results:") 
#print(z)
#print("Standard deviation of unhealthiest students results:") 
#print(y)


porEduFathers <- por[por$Fedu == "4",]
porEduMothers <- por[por$Medu == "4",]
porlowEduFathers <- por[por$Fedu == "0",]
porlowEduMothers <- por[por$Medu == "0",]

porHighParents <- por[((por$Fedu + por$Medu) >= 6),]
porLowParents <- por[((por$Fedu + por$Medu) <= 2),]

matHighParents <- mat[((mat$Fedu + mat$Medu) >= 6),]
matLowParents <- mat[((mat$Fedu + mat$Medu) <= 2),]

#jacobs alcohol consumption one 
#boxplot(mat$G3[(mat$Walc + mat$Dalc) <= median(mat$Walc + mat$Dalc)], mat$G3[(mat$Walc + mat$Dalc) > median(mat$Walc + mat$Dalc)], por$G3[(por$Walc + por$Dalc) <= median(por$Walc + por$Dalc)], por$G3[(por$Walc + por$Dalc) > median(por$Walc + por$Dalc)], las=2, col=c("royalblue2", "palevioletred1", "royalblue2", "palevioletred1"), at=c(1,2, 4,5), names=c("Maths (low alc)", "Maths (high alc)", "Por (low alc)", "Por (high alc)"), ylab="Final Grade (0 - 20)")

#boxplot health vs absences for portugese and maths 
#boxplot(unhealthyPor$absences, healthyPor$absences, unhealthyMat$absences, healthyMat$absences, names = c("Un-healthy Por","healthy Por","Un-healthy Maths","healthy Maths"),las=2, at=c(1,2, 4,5))

#boxplot low educated parents living apart vs high educated parents living together for portugese - dosnt work for maths, theres no maths low educated parents living apart
boxplot(porLowParents$G3[porLowParents$Pstatus == "A"], porHighParents$G3[porHighParents$Pstatus == "T"], names = c("Low edu parents living apart","High edu parents living together"))











