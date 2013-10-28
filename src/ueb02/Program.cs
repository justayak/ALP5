namespace Check
{
    using Check.Properties;
    using System;
    using System.Collections.Generic;
    using System.IO;
    using System.Linq;
    using System.Text;
    using System.Text.RegularExpressions;
    using System.Threading.Tasks;

    class Program
    {
        static void Main(string[] args)
        {
            // -- Check and read params 
            if (args.Length != 3) throw new ArgumentException("3 arguments expected! -> file from to");
            var file = args[0];
            if (!File.Exists(file)) throw new ArgumentException("Could not find File {" + file + "}");
            var fromPos = args[1].ToInt();
            var to = args[2].ToInt();       

            // -- the actual work
            var lookup = (from word in Resources.german.Split('\n')
                          select word.Trim()).ToHashSet();

            var text = from word in File.ReadAllText(file)
                           .Substr(fromPos, to)
                           .Clean()
                           .Split(new char[] { ' ', '\n' })
                       where !String.IsNullOrEmpty(word)
                       select word.Trim();

            foreach (var word in text)
            {
                if (!lookup.Contains(word)) Console.WriteLine(word);
            }
        }
    }

    /// <summary>
    /// Just some helper code to make code above easier to read...
    /// </summary>
    static class Extensions
    {
        public static int ToInt(this String str)
        {
            return Convert.ToInt32(str);
        }

        public static String Clean(this String str)
        {
            return Regex.Replace(str
                .Replace(".", " ")
                .Replace("-", " ")
                .Replace(",", " ")
                .Replace("\r", " ")
                .Replace(":", " "), @"[\d]", String.Empty);
        }

        public static String Substr(this String str, int from, int to)
        {
            return str.Substring(from, (to - from) + 1);
        }

        public static T[] SubArray<T>(this T[] data, int i, int l)
        {
            T[] result = new T[l];
            Array.Copy(data, i, result, 0, l);
            return result;
        }

        public static HashSet<T> ToHashSet<T>(this IEnumerable<T> source)
        {
            return new HashSet<T>(source);
        }
    }
}
