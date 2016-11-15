**CA4010 Data Mining Assignment**

# Idea and Dataset Description
We obtained our dataset from the [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets/Student+Performance). It contains information about students in two Portuguese secondary schools. The dataset contains 32 attributes for each student...

Our idea is to predict a student's academic performace by looking at their background, school life, and their habits. 


# Data Preparation
We didn't need to alter our dataset too much as there were no missing values and the format made logical sense. We did, however, replace all string values with integer representations to make it easier to process.

Our workshop on data dispersion helped us to gain an insight into how different attributes affect student performance and this helped us to define the classes we were trying to assign students to. Student grades are on a scale from 0 to 20 so there were initially 21 discrete target values. We were hoping to group these into classes of 3 grades (`{0,1,2}, {3,4,5}, ..., {18,19,20}`) but our analysis for our workshop helped us to realise that it is too difficult to differentiate between a student who might receive a grade of 9 and a student who might receive a grade of 13. 

![dispersion of results](link)

You can see from the diagram above that nearly `(60%)` of students receive grades between `(10 and 15)`.


# Algorithm Description
Our project was a classic supervised learning example and we felt a classification approach was more suitable than clustering.

We implemeneted the k-nearest neighbours algorithm, using a weighted Euclidian distanca as our distance metric. The weightings were assigned by an algorithm we designed which altered the weighting and compared the results to previous itereations of the prediction algorithm.

As this was a computationally intesive algorithm to run, it would be too time consuming to run every time we wanted to run the prediction algorithm so we separated the two procedures. The weighting decider would output the weightings to a text file and the prediction algorithm would import them from the same file.

# Results and Analysis