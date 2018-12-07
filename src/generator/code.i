var x: real is 1
var M: integer is 6
var N: integer is 104
var struct : record
				var current: real is 1.0
				var next: real
				var result: real
			end


routine get_power(m:integer):integer is
	var k: integer is 1
	for i in 1..m loop
		k := k * 2
	end

	var result is k
end

routine choose_next(ns: integer):real is
	for i in 55..n loop
		 ns := ns + i-55 + i-24 % get_power(M)
	end

	var result is ns
end


routine laggedFib(n: integer): real is
	x_next: struct
	if n<56 then
		x_next.next := 0.0
	else
		x_next.next := x_next.next +choose_next(x_next.current) + choose_next(x_next.current) % get_power(M)
	end

	x_next.result := x_next.next
end

routine main():real is
	var result is choose_next(N)
end
