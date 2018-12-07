var x: real is 1.0
var M: integer is 6
var n: integer is 104
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

	var result:integer is k
end

routine choose_next(ns: integer): integer is

	for i in 55..n loop
		 ns := ns + i-55 + i-24 % get_power(M)
	end

	var result:integer is ns
end


routine main():integer is
	var result is choose_next(n)
end
