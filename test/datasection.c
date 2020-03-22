#include <stdio.h>
const char* messages[] = {
    "You passed no arguments\n",
    "You passed one argument\n",
    "You passed two arguments\n",
    "You passed three arguments\n",
    "You passed four arguments\n",
    "You passed five arguments\n",
    "You passed six arguments\n",
    NULL
};

int main(int argc, char** argv){
	if(argc > 6) return 0;
	puts(messages[argc]);
	return 0;
}
