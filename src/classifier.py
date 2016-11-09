import numpy as np
import urllib

mat = open("../datasets/student-mat-normalised.csv")
# por = open("../datasets/student-por.csv")

dataset = np.loadtxt(mat, dtype=int, delimiter=",", skiprows=1)

X = dataset[:,0:30]
y = dataset[:,32]

# create target classes
for i in range(0,len(y)):
    if y[i] < 7:
        y[1] = 0
    elif y[i] < 14:
        y[i] = 1
    else:
        y[i] = 2

# partition into training and testing sets
from sklearn.cross_validation import train_test_split
 
# test_size=0.5 -> split in half
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.5)
 
# Classifier
from sklearn import tree

my_classifier = tree.DecisionTreeClassifier()
my_classifier.fit(X_train, y_train)

# predict
predictions = my_classifier.predict(X_test)
print(predictions)

# test
from sklearn.metrics import accuracy_score

print(accuracy_score(y_test, predictions))

# Repeat using KNN
# Classifier
from sklearn.neighbors import KNeighborsClassifier

my_classifier = KNeighborsClassifier()
my_classifier.fit(X_train, y_train)

# predict
predictions = my_classifier.predict(X_test)
print(predictions)

# test
from sklearn.metrics import accuracy_score

print(accuracy_score(y_test, predictions))
