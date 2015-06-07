import re
import glob

if __name__ == "__main__":
	filelist = glob.glob('*.txt')
	elitelist = []
	for item in filelist:
		f = open(item)
		line = f.readline()
		r = re.compile("gen:(\d+) genes:(\d+) ")

		m = r.search(line)
		generation = int(m.group(1))
		genes = int(m.group(2))
		l = ((genes + 1) * generation + 4)

		while(l > 0):
			line = f.readline()
			l -= 1

		print line
		elitelist.append(line)

	elitelist.sort()
	elitelist.reverse()
	wf = open('elites.txt', 'w')
	for item in elitelist:
		wf.write(item)
	f.close()
	wf.close()
