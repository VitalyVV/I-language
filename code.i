routine fibIter(prev: integer, current: integer, n: integer): integer is
    var result: integer
    if n = 0 then
        result := current
    else
        result := fibIter(current, prev + current, n - 1)
    end
    result := result
end


routine fibTailRecursion(n: integer): integer is
    var result: integer
    if n > 1 then
        result := fibIter(1, 1, n - 2)
    else
        result := n
    end
   result := result
end

var alo : integer is fibTailRecursion(10)