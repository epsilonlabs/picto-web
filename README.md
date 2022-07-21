# [Picto Web](#contents)

## [Contents](#contents)
- [Picto Web](#picto-web)
  - [Contents](#contents)
  - [Introduction](#introduction)
  - [Docker](#docker)
  - [Demo](#demo)

## [Introduction](#contents)
[Picto Web](https://github.com/epsilonlabs/picto-web) is a tool for complex model exploration. With it, users can visualise large models in different views and formats and also to perform drill down/up navigation throughtout the models' elements. Picto Web is a the web version of the [Picto](https://www.eclipse.org/epsilon/doc/picto/) for Eclipse. It aims to supports all features that the plugin capable of except that it is a web application, which allows multiple, remote users to explore a same model concurrently. 

The app is able to read *.picto files and display the view trees and views of the files. It is also able to monitor files in a directory if they have changed and send the updates to the front-end for  auto-refresh. Picto Web also refactors [Picto](https://www.eclipse.org/epsilon/doc/picto/) so that its core libaries is independent of the Eclipse platform. The app is packaged in a [Docker container](https://hub.docker.com/r/alfayohannisyorkacuk/picto-web).

## [Docker](#contents)
The docker image of Picto Web can be temporarily found here: [https://hub.docker.com/r/alfayohannisyorkacuk/picto-web](https://hub.docker.com/r/alfayohannisyorkacuk/picto-web). Execute the following command to pull it from https://hub.docker.com:
```
docker pull alfayohannisyorkacuk/picto-web
```

## [Demo](#contents)
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
docker run --rm -i -t -v %cd%:/workspace --hostname=picto -p 8080:8080 --name=picto alfayohannisyorkacuk/picto-web
```
Replace `%cd%` with `$PWD`, if Linux is your current operating system, 
```
docker run --rm -i -t -v $PWD:/workspace --hostname=picto -p 8080:8080 --name=picto alfayohannisyorkacuk/picto-web
```
or replace it with any directory path if you want to set the directory as the working directory of Picto Web. Basically, the command maps the local host directory to the internal `workspace` directory in the Docker container so that the local host directory can be accessed by the Picto Web app in the Docker container.

Browse http://localhost:8080 to load Picto Web app. It will display the page that shows picto files in the mapped directory.

![main page](images/main_page.png)

Click the `the socialnetwork.model.picto` link. It will open another tab that shows the view tree of Picto Web as displayed below.