# A Static View
This static view is loaded from a Markdown file, `readme.md`. 
The file displays a static image, `Invoice`'s class diagram, and embedded PlantUML script.

<table>
<tr>
<td>

![Epsilon logo](pos/epsilon.png)

</td>
<td>

<picto-view path="Point of Sales, pos.Invoice"/> 

</td>
<td>

```render-plantuml
@startuml
Legume <|-- RedBean
Legume <|-- MungBean
@enduml
```

</td>
</tr>
</table>