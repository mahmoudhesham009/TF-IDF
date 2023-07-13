# TF-IDF
## _TF-IDF searching algorithm running on a cluster of nodes using zookeeper_

![N|Solid](https://cdn-media-1.freecodecamp.org/images/vQ77VuGVlTR95GgMxzyKqydIqoRJcPcWrigy)




TF-IDF stands for Term Frequency - Inverse Document Frequency. It is a statistical measure that is used to evaluate the importance of a word in a document. TF-IDF is calculated by multiplying the term frequency (TF) by the inverse document frequency (IDF).

The term frequency (TF) is the number of times a word appears in a document. The inverse document frequency (IDF) is a measure of how rare a word is in a corpus of documents. The more rare a word is, the higher its IDF will be.

The TF-IDF score for a word is calculated as follows:

TF-IDF = TF * IDF
The TF-IDF score is a measure of how important a word is in a document, relative to how important it is in other documents.

TF-IDF searching algorithm is a technique that uses TF-IDF scores to rank documents in a search results list. The algorithm works by first calculating the TF-IDF scores for all of the words in the query. Then, the algorithm calculates the TF-IDF scores for all of the words in the documents in the corpus. Finally, the documents are ranked in the search results list in descending order of their TF-IDF scores.

TF-IDF searching algorithm is a popular technique for information retrieval. It is a simple and effective way to rank documents in a search results list. However, it can be computationally expensive to calculate TF-IDF scores for a large corpus of documents.

Here are some of the advantages of using TF-IDF searching algorithm:

It is a simple and effective way to rank documents in a search results list.
It is relatively insensitive to the length of documents.
It can be used to compare documents that are of different sizes.
Here are some of the disadvantages of using TF-IDF searching algorithm:

It can be computationally expensive to calculate TF-IDF scores for a large corpus of documents.
It is not always clear how to choose the optimal values for the TF and IDF parameters.
Overall, the TF-IDF searching algorithm is a powerful and versatile technique for ranking documents in a search results list. It is a popular choice for a variety of information retrieval tasks.

in this project, we run that Document searching algorithm on distributed nodes using a zookeeper as co-coordinator to make it faster and more efficient.



## How it works

- all nodes are registered as children for /election znode to elect one of the nodes to be the leader and the rest of the nodes will become workers.
- the worker nodes will be registered as a child for /server_register, start web server znode with and store the endpoint for this node inside its data.
- the leader node will be registered as a child for /co_server_register, start web server znode with and store the endpoint for this node inside its data.
- when [front-end application](https://github.com/mahmoudhesham009/TF-IDF_FrontEnd) gets a request, it sends a request to the node registered as a child in the /co_server_register znode and sends the search terms to the child which is the leader node.
- the leader node will get the child in the /server_register znode and distribute the tasks between them.
- workers will take tasks and calculate the terms frequency of each document and return the results based on [TF-IDF algorithm](https://en.wikipedia.org/wiki/Tf%E2%80%93idf/).
- after the leader, node gets results from each node, calculates inverse document frequency, and calculates the final score for each node, it sends the response to the front-end to display it.


## How to run it

- clone the project.
- copy and paste the documents you wanna search in the books file.
- package project jar file using maven.
- run jar file multiple times on different ports by passing the port number in the program args.
```cmd
java -jar tf-idf-1.0-SNAPSHOT-jar-with-dependencies.jar 8085
```

- run the [front-end applicatoin](https://github.com/mahmoudhesham009/TF-IDF_FrontEnd).
- send get request containing terms as a query to the front-end app.

