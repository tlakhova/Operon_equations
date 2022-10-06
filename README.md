# Operon_equations

Algorithm for reconstruction of mathematical frame models of bacterial transcription regulation was implemented as the Operon_equations. 
The programm is written in Java.

**Abbreviations**

transcription factors - TF

transcription factors binding sites - TFBS

# Input data

Operon_equations requers text file with tabular separator. This file including information about operon name,TF name regulating the operons,
left and right TFBS positions (absolute positions in genome of the sites) and regulation type of the TF. This file have not title.

**Example of input data**

```
fabA  FabR  1016509 1016526 repressor
fabA  FadR  1016528 1016544 activator
```
If regulation type of the TF is unknown you are to write "regulation" and if you have information of the dual function of the TF - "dual".
In case when TFBS positions are also unknown are written zeros such as in 3rd and 4th columns.
For example:

```
Operon_1  TF1 0 0 regulation
Operon_2  TF2 0 0 dual
```

# Output data

Operon_equations outputs text files with tabular separator. The file includes information about generated or blocked states, the number of variables, parameter description and form of equation from [1].

**Example of output data**

```
Mathematical model transcription of the operon: **operon_name**

Generated  states: **number_of_generated_states**

Blocked states: **number_of_generated_states**

======================

variables: **number_of_variables**

1. (The variables are described here)
2.  ...

parameters: **number_of_parameters**

V_0	;	Initial rate

a_basal	;	Basal activity

k_i	;	TF activity

n_i<comples_index>	;	 TF (it number is written across #) Hill parameter

level_i;	 TF (it number is written across #) rate activity parameter

w_ij	;	 cooperation parameter (There is parameter when TFBS do not intercross)

Numerator = (generating of equation numerator)

Denominator = (generating of equation denominator)

f = V_0*Numerator/Denominator (total equation)
```

[1] Likhoshvai, V.; Ratushny, A. Generalized Hill Function Method for Modeling Molecular Processes. J. Bioinform. Comput. Biol. 2007, 05, 521–531, doi:10.1142/S0219720007002837.







