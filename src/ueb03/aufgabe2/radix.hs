f :: Int -> ([Int],[Int]) -> ([Int],[Int])
f s(in0,in1)= collate(distribute in0)(distribute in1)
              where collate(a,b)(c,d) = (a++c,b++d)
                    distribute [] = ([],[])
                    distribute(i:is)
                        | bit == 0  = (i:a, b)
                        | bit == 1  = (a, i:b)
                          where (a,b) = distribute is
                                bit = (i `div` 2^s)`mod` 2