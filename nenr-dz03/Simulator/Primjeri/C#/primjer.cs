using System;
using System.Text;
using System.IO;

namespace fuzzyControlSysCS
{
    class Program
    {
        static void Main(string[] args)
        {   
            int L, D, LK, DK, V, S, akcel, kormilo;

	        while(true){                         
                String str = Console.ReadLine();
                if (str[0] == 'K') break;
                String[] p = str.Split(' ');
                L = int.Parse(p[0]);
                D = int.Parse(p[1]);
                LK = int.Parse(p[2]);
                DK = int.Parse(p[3]);
                V = int.Parse(p[4]);
                S = int.Parse(p[5]);
	            
	            // fuzzy magic ...

                akcel = 10;  kormilo = 5;
                Console.Write(akcel.ToString() + " " + kormilo.ToString() + "\r\n");
                Console.Out.Flush();	            
	        }
        }
    }
}
