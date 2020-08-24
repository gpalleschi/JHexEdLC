# JHexEdLC

---
## Description

**A very effective JAVA line command tool to edit ascii or binary files, this tool permits to insert or update a value specify an hexadecimal value or delete a portion of it from an offset to other one.**

Use: `java -jar JHexEdLC.jar <Input File>` `[[-i]` `[-u]` `[-d]]` `[-b<offset start>]` `[-e<offset end>]` `[v<value in hex format>]` `[-o<Output File>]` `[-h]`

Parameters `[...]` are optional
Parameters `<...>` are mandatory

**`<Input File>`**               : Input File 
**`[-i]`**                       : Insert Modality *(specify -b parameter and -v for the value)*  
**`[-u]`**                       : Update Modality *(specify both -b and -e parameters and -v for value)*   
**`[-d]`**                       : Delete Modality *(specify both -b and -e parameters)*    
**`[-b<offset start>]`**         : Offset from  
**`[-e<offset end>]`**           : Offset to  
**`[-v<hexadecimal value>]`**    : Hexadecimal value used in modality UPDATE and INSERT    
**`[-o<Output File>]`**          : Output File  
**`[-h]`**                       : Show an help    

---
## Getting Started

Copy jar on your local machine. Run it "**java -jar JHexEdLC.jar .....**"

---
### Prerequisites

`>= Java 1.7`  

## Built With

* [Eclipse](https://www.eclipse.org/) 

---
## Authors

* **Giovanni Palleschi** - [gpalleschi](https://github.com/gpalleschi)

---
## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE 2.0 License - see the [LICENSE](LICENSE) file for details
