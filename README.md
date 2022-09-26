# Operon_equations

Algorithm for reconstruction of mathematical frame models of bacterial transcription regulation was implemented as the Operon_equations. 
The programm is written in Java.

**Abbreviations**

transcription factors - TF

transcription factors binding sites - TFBS

# Input data

Operon_equations requers text file with tabular separator. This file including information about name operons, name TF regulating the operons,
left and right positions TFBS (absolute positions in genome of the sites) and regulation type of the TF. This file have not title.

**Example of input data**

```
fabA  FabR  1016509 1016526 repressor
fabA  FadR  1016528 1016544 activator
```
If regulation type of the TF is unknown you are to write "regulation" and if you have information of the dual function of the TF - "dual".
There are times when TFBS positions are also unknown. In this case in 3rd and 4th columns are written zeros.
For example:

```
Operon_1  TF1 0 0 regulation
Operon_2  TF2 0 0 dual
```
