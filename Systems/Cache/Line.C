#include <iostream>
#include <string>
#include <assert.h>
#include "Line.h"

//Move along. Nothing to do here.
//okay🧙‍♂️

/*
 * Line
 * Initializes the tag field and the next fields
 * of a newly constructed Line object
 */
Line::Line(int64_t tag)
{
   this->tag = tag;
   this->next = NULL;
}
