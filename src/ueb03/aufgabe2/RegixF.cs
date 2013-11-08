using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RaddixF
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length < 2)
            {
                args = new String[] {"[0,0,1]", "[1,1,0]" };
            }


            var in0 = args[0].ToIntArray();
            var in1 = args[1].ToIntArray();



        }

        static Tupel Distribute(int[] i)
        {
            if (i.Length == 0)
            {
                return new Tupel(new int[0], new int[0]);
            }

            var tail = i.Skip(1).ToArray();


            //return null;
        }

        static Tupel Collate(int[] a, int[] b, int[] c, int[] d)
        {
            var left = Concat(a, b);
            var right = Concat(c, d);
            return new Tupel(left, right);
        }

        static int[] Concat(int[] a, int[] b)
        {
            var result = new int[a.Length + b.Length];
            a.CopyTo(result, 0);
            b.CopyTo(result, a.Length);
            return result;
        }

    }

    struct Tupel
    {
        public int[] First;
        public int[] Second;
        public Tupel(int[] a, int[] b)
        {
            First = a;
            Second = b;
        }
    }


    /// <summary>
    /// Helper stuff
    /// </summary>
    static class Extensions
    {
        public static int[] ToIntArray(this String str)
        {
            return (from p in str.Replace("[", "").Replace("]", "").Split(',')
                   select Convert.ToInt32(p)).ToArray();
        }
    }
}
