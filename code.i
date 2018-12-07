var size: integer is 10
var a: array[size] integer


routine swap(a:integer, b:integer): array[2] integer is
    var swapped: array [2] integer
    swapped[1]:= b
    swapped[2]:= a
    var result: array [2] integer is swapped
end

routine sort(a : array [size] integer):array[size] integer is
	for i in 1..size loop
		for j in 1..size loop
			if a[j] < a[i] then
				var temp: integer is a[j]
                var swapped: array [2] integer is swap(a[j],a[i])
                a[j]:= swapped[1]
                a[i]:= swapped[2]
			end
		end
	end
	var result: array[size] integer is a
end

routine main() is
    a[1]:= 1
    a[2]:= 2
    a[3]:= 3
    a[4]:= 4
    a[5]:= 5
    a[6]:= 6
    a[7]:= 7
    a[8]:= 8
    a[9]:= 9
    a[10]:= 10
    var result is sort(a)
end