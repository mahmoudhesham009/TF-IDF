# TF-IDF
## _TTF-IDF searching algorithm running on cluster of nodes using zookeeper_

![N|Solid](https://cdn-media-1.freecodecamp.org/images/vQ77VuGVlTR95GgMxzyKqydIqoRJcPcWrigy)



•	This Document searching algorithm running on distributed nodes using zookeeper as co-coordinator



## How it works

- all nodes are registered as children for /election znode to elect one of the nodes to be the leader and the rest of the nodes will become workers.
- the worker nodes will be registered as a child for /server_register, start web server znode with and store the endpoint for this node inside its data.
- the leader node will be registered as a child for /co_server_register, start web server znode with and store the endpoint for this node inside its data.
- when [front-end applicatoin](https://github.com/mahmoudhesham009/TF-IDF_FrontEnd) get a request, it sends a request to the node registered as a child in the /co_server_register znode and send the search terms to the child which is the leader node.
- the leader node will get the child in the /server_register znode and distribute the tasks between them.
- workers will take tasks and calculate the terms frequency of each document and return the results based on [TF-IDF algorithm](https://en.wikipedia.org/wiki/Tf%E2%80%93idf/).
- after the leader node get results from each node, calculate inverse document frequency and calulate the final score for each node, it sends the response to the front-end to display it.


## How to run it

- clone the project.
- copy and past the documents you wanna search in in the books file.
- package project jar file using maven.
- run jar file muliple time on different ports by pass the port number in the program args.
```cmd
java -jar tf-idf-1.0-SNAPSHOT-jar-with-dependencies.jar 8085
```

- run the [front-end applicatoin](https://github.com/mahmoudhesham009/TF-IDF_FrontEnd).
- send get request contain terms as query to the front-end app.

