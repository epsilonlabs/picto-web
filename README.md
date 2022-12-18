# [Picto Web](#contents)

## [Contents](#contents)
- [Picto Web](#picto-web)
  - [Contents](#contents)
  - [Introduction](#introduction)
  - [Highlights](#highlights)
  - [Docker](#docker)
  - [Demo (up and running)](#demo-up-and-running)
    - [Automatic Client Update](#automatic-client-update)
    - [Different Views](#different-views)
    - [Another Example](#another-example)
  - [Build the Docker Container](#build-the-docker-container)
  - [Examples](#examples)
  - [Other Resources](#other-resources)
    - [Videos](#videos)
    - [Publications](#publications)
    - [Slides](#slides)

## [Introduction](#contents)
[Picto Web](https://github.com/epsilonlabs/picto-web) is a tool for complex model exploration. With it, users can visualise large models in different views and formats and also perform drill down/up navigation throughout the models' elements. Picto Web is the web version of the [Picto](https://www.eclipse.org/epsilon/doc/picto/) for Eclipse. It aims to support all features that the plugin is capable of, except that it is a web application, which allows multiple remote users to explore the same model concurrently. 

The app can read *.picto files and display the view trees and views of the files. It can also monitor files in a directory if they have changed and send the updates to the front-end for auto-refresh. Picto Web also refactors [Picto](https://www.eclipse.org/epsilon/doc/picto/) so that its core libraries are independent of the Eclipse platform. The app is packaged in a [Docker container](https://hub.docker.com/r/alfayohannisyorkacuk/picto-web).

## [Highlights](#contents)
Here are some of the highlights of Picto Web:
1. Picto Web is a multi-tenant client-server web application that can be accessed through a standard web browser to perform interactive complex model exploration. It enables users to contextualise complex models at various granularities and viewpoints (multi-views, drilling-down/up tree views).
2. It allows for the visualisation of models in various representation forms (e.g., HTML, Graphviz, SVG, PlantUML, etc.).
3. It offers viewers a multi-user environment and immediate propagation of model modifications.
4. It supports the lazy generation of views to reduce upfront computing costs (see [Architecture](docs/architecture.md) and [Access Resource](docs/accessgraphresource.md)).


## [Docker](#contents)
The docker image of Picto Web can be temporarily found here: [https://hub.docker.com/r/alfayohannisyorkacuk/picto-web](https://hub.docker.com/r/alfayohannisyorkacuk/picto-web). Execute the following command to pull it from https://hub.docker.com:
```
docker pull alfayohannisyorkacuk/picto-web
```

## [Demo (up and running)](#contents)
Clone Picto Web project from GitHub using the command below 
```
git clone https://github.com/epsilonlabs/picto-web
```
Change directory to sub-directory `workspace`.
```
cd picto-web/workspace
```
If your operating system is Windows, run the command below to run the Picto Web server. The variable `%cd%` represents your current working directory in Windows. 
```
docker run --rm -i -t -v %cd%:/workspace --hostname=picto -p 8081:8081 --name=picto alfayohannisyorkacuk/picto-web
```
Replace `%cd%` with `$PWD` if Linux is your current operating system, 
```
docker run --rm -i -t -v $PWD:/workspace --hostname=picto -p 8081:8081 --name=picto alfayohannisyorkacuk/picto-web
```
Or replace it with any directory path if you want to set the directory as the working directory of Picto Web. The command maps the local host directory to the internal `workspace` directory in the Docker container. Therefore, the Picto Web app in the Docker container can access the local host directory. The commands will display output like the following.

![main page](images/docker.png)

Browse http://localhost:8081 to load the Picto Web app. It will display the `main` page that shows *.picto files in the mapped directory.

![main page](images/main_page.png)


### [Automatic Client Update](#contents)

Click the `sndemo.picto` file. It will open another tab that shows the view tree of Picto Web and then click the `Social Network` node to display the page below.

![main page](images/socialnetwork-01.png)

Go back to the `main` page and click again the `sndemo.picto` link. It will open the same tab as before. Now, click the node `Stats` on the tree. It will display the following table.

![main page](images/table-01.png)

After that, open any text editor. Open the `sndemo` file and then change the value of Alice's `name` attribute from `Alice` to `Amber`. Picto Web automatically sends the change to the clients to update `Alice` to `Amber`.

**IMPORTANT! If you use the Docker container, this automatic client update only works on Linux OS. Since the Docker container uses Linux as the OS, it can only detect changes in files on the same OS; changes cannot be detected on Windows. However, if Picto Web runs directly from its source code (not using Docker container) on Windows, changes on files can be detected perfectly as well.**

### [Different Views](#contents)

Picto Web also supports displaying custom views and drilling down views of a model. The custom view is displayed in the following image, which only shows the network of `Alice` and `Bob`.

![main page](images/custom.png)

Also, explore the view tree by clicking any of the nodes. For example, the below figure shows the view of the `Charlie` node.

![main page](images/drilldown.png)

### [Another Example](#contents)
Besides the `social network` example, Picto Web also has been able to visualise `Ecore` metamodel in the documentation-like format in HTML.

![main page](images/ecore.png)

## [Build the Docker Container](#contents)

Follow the following steps to build the Picto Web Docker container on your local machine:

1. Clone the source code from GitHub:
   ```shell
   git clone https://github.com/epsilonlabs/picto-web
   ```
1. Change directory to `picto-web`:
   ```shell
   cd picto-web
   ```
1. Build the image:
   ```shell
   docker compose build
   ```
1. Run a container from the image:
   ```shell
   docker compose up
   ```
1. Stop the container with Ctrl+C.

## [Examples](#contents)
1. [ecorediagram](workspace/ecorediagram/)
2. [egldoc](workspace/egldoc/)
3. [performance](workspace/performance/)
4. [scpdemo](workspace/scpdemo/)
5. [socialnetwork](workspace/socialnetwork/)
   
## [Other Resources](#contents)
### [Videos](#contents)

The demo videos of Picto Web can be found at:
1. Early prototype demos: https://shorturl.at/ijtz1 or https://drive.google.com/drive/folders/1BCEKJhsEq50Yi7xFjx2Wpc6j-68Wtcxv?usp=sharing.

### [Publications](#contents)
1. Alfa Yohannis, Dimitris Kolovos, Antonio García-Domínguez, and Carlos Javier Fernández Candel. 2022. Picto web: a tool for complex model exploration. In Proceedings of the 25th International Conference on Model Driven Engineering Languages and Systems: Companion Proceedings (MODELS '22). Association for Computing Machinery, New York, NY, USA, 56–60. https://doi.org/10.1145/3550356.3559094

### [Slides](#contents)
1. Picto Web has been presented at the [MODELS 2022](https://conf.researchr.org/track/models-2022/models-2022-tools---demonstrations?#event-overview) conference https://www.slideshare.net/bluezio/models-2022-picto-web-tool-demo.

