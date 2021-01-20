//primjer ne previse pametnog sustava upravljanja
//ovaj sustav stalno ubrzava i skrece lijevo

#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[])
{
	char buff[1000];
	int L,D,LK,DK,V,S,kormilo,akcel;

	while(true){
	  fgets(buff,1000,stdin);
	  if(buff[0]=='K') break;	  
	  sscanf(buff,"%d %d %d %d %d %d",&L,&D,&LK,&DK,&V,&S);
	  
	  // fuzzy magic ...

	  akcel = 10, kormilo = 5;
	  fprintf(stdout,"%d %d\n",akcel,kormilo);
	  fflush(stdout);
	}
}
