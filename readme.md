# XpressC

### Summary
This is a Compiler for Business Doc Templates (BDT). Bdt's can be assembled as in the Xpression Application with additional checks and tests.


### Notes on Object Models 

####  CR Query
The BDT XML structure implies that conditional blocks will be evaluated out into a content piece, however each InsertTextpiece is preceded by a TextClass ID conditional within a seperate IF block to the actual conditions that would play the content.
This means that no matter what the condition is to play the content, the InsertTextpiece will ALWAYS play, becuase the Textclass ID conditional is ALWAYS true (it is always a conditional that checks whether the Textclass ID is not equal to 0 - which it never is).
To get around this, the CRQuery block will be co-opted as a conditional check to play a Textpiece.
The CRQUERY is always within the same true block of a conditional check so if the CRQUERY holds a value > the Textpiece will play, if the CRQUERY does not hold a value the TextPiece wont play.
THis won't be to far off of what xpression is doing behind the scenes but it is not a 1-to-1 representation.