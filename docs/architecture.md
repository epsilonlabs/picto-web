# Architecture

![architecture](../images/architecture.svg)

The incremental regeneration of Picto Web's views follows the steps in the figure above. Picto Web has a singleton called `FileWatcher` that is responsible for monitoring changes on model files in a working directory, including creating and deleting a file (**step 1**). Every change to a file -- when the file is saved -- will trigger Picto Web to load the associate model transformation (EGX) file and other required files (e.g., the related template, model, metamodel files) into the memory and perform transformation `T1` (**step 2**). If the change is the deletion of a file, the `FileWatcher` will remove all related access records and view cache associated with the file (**step 3**).

Picto Web adopts the lazy transformation approach. The text generation in EGX/EGL commonly takes rules and templates as its inputs. When executed, the generation immediately processes them to produce the output files. However, in the lazy approach, only the rules are processed immediately to produce promises (**step 4**). Promises are the entities that will be transformed into views later in step 9. In other words, a promise only holds a reference to the template, but the template has not been processed yet to generate the target file. Every rule can produce more than one promise, and every promise is associated with a view, and it uses the path of the view in the tree view as its identifier. 

When performing the generation of promises in step 4, Picto Web records accesses to model elements and their properties and stores the accesses to the `AccessResource` (**step 5**). The `Optimiser` collects all the necessary information from the access resource and uses it to optimise the regeneration of views. In **step 6**, the `Optimiser` filters the promises and only selects them that will produce views affected by the changes applied to the files in step 1. Moreover, using the information in the `AccessResource`, the `Optimiser` also removes access records in the `AccessResource` and views in the `ViewContentCache` that are associated with elements that has been deleted to free memory space (**steps 7 and 8**). 

Similar to step 5, when the transformation `T2` processes the templates of the filtered promises to generate the target views (**step 9**), Picto Web also records element and property accesses and stores them to the `AccessResource` (**step 10**). Not all views are generated since the promises have been filtered by the `Optimiser`. After that, the generated new views are the store into the `ViewContentCache` (**step 11**). The new views also are sent to the `Broker` -- a broker server --  to be retrieved later by clients (step 12).

A client that accesses Picto Web using a web browser automatically subscribes to the `Broker` (**step 13**). Therefore, it retrieves the updated views immediately once a model file is changed, giving users a live visualisation experience of model editing (**step 14**). Also, the client can retrieve a specific view by sending a request to the web server (s**tep 15**). The web server then retrieves the requested view from the `ViewContentCache` (**step 16**) and sends it back to the client as a response (**step 17**).

```java
/* Logics in the filter -- Checking if a view/path needs (re)generation: new, needs update, or deletion */

function boolean isViewNewOrUpdated(String checkedPath, EgxModule currentModule) {

  /* new path */
  if path is not in the property accesses (PA) {
    return true;
  `

  /* new object */
  //1. As long as the addition of a new object changes the state of, at least, another object's property that has been accessed previously, adding a new object is handled in the 'modified property' condition below.
  

  /* deleted object */
  if the object associated with the path in the PA does not exists, null value {
    return true;
  `

  /* modified property */
  if the current value of a property associated with the path is not equal to its respective value recorded in the PA {
    return true;
  `
  ...
`
```

## More
* [AccessGraphResource](accessgraphresource.md)