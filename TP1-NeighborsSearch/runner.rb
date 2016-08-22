particles = 100
l = 20
rc = 1
max_radio = 0.25
cells = (2..10).to_a

module Enumerable
	def sum
		self.inject(0){|accum, i| accum + i }
	end
	def mean
		self.sum/self.length.to_f
	end
	def sample_variance
		m = self.mean
		sum = self.inject(0){|accum, i| accum +(i-m)**2 }
		sum/(self.length - 1).to_f
	end
	def standard_deviation
		return Math.sqrt(self.sample_variance)
	end
end 

system("rm -f total.brute.#{particles}")
system("rm -f total.cim.#{particles}")


cells.each do |m|
  system("rm -f cim.#{m}")
  system("rm -f bruteforce.#{m}")
  system("touch cim.#{m}")
  system("touch bruteforce.#{m}")
  10.times do |i|
    #puts "m:#{m} - i: #{i}"
    system("rm -f particles.tmp")
    system("java -jar target/TP1-NeighborsSearch-1.0-SNAPSHOT.jar gen #{particles} #{l} #{max_radio} > particles.tmp")

    system("java -jar target/TP1-NeighborsSearch-1.0-SNAPSHOT.jar find #{l} #{m} #{rc} 1 0 particles.tmp > /dev/null 2>>cim.#{m}")
    system("java -jar target/TP1-NeighborsSearch-1.0-SNAPSHOT.jar find #{l} #{m} #{rc} 1 1 particles.tmp > /dev/null 2>>bruteforce.#{m}")
  end

  brute = IO.read("bruteforce.#{m}").lines.map { |l| l.match(/[0-9]+/).to_s.to_i }
  cim = IO.read("cim.#{m}").lines.map { |l| l.match(/[0-9]+/).to_s.to_i }

	system("echo #{brute.mean},#{brute.standard_deviation},#{m} >> total.brute.#{particles}")
	system("echo #{cim.mean},#{cim.standard_deviation},#{m} >> total.cim.#{particles}")
end


	
