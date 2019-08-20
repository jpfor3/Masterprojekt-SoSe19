package Signatures;

public class NN_Signatur {
	
	//TODO: Verstehen, wie Signaturen generiert werden können (untenstehendes Beispiel beachten)

}


/*

Aus dem Buch (Seite 309, Kap. 7.1 'Hochdimensionale Indexstrukturen'): Aufbau des Signaturalgorithmus:

Anm.: lb = minimale Distanz; ub = maximale Distanz

[1] procedure Signatur-nn(punkt q, signaturliste B,
[2]			punkte P, punkte nachsterNachbar)
[3]		punktliste kandidaten = nil
[4]		real distanz = maxreal
[5]		for each b in B do // In Listenreihenfolge
[6]			if lb(q,b) <= distanz then do
[7]				if ub(q,b) <= distanz then do
[8]					distanz = ub(q,b)
[9]					entferne bi's aus kandidaten mit lb(q,bi)>distanz
[10]			end if
[11]			kandidaten = append(kandidaten,b)
[12]		end if
[13]	end for
[14]	sortiere b in kandidaten nach lb(q,b) aufsteigend
[15]	for each b in kandidaten do //sortierter Zugriff
[16]		if lb(q,b) <= distanz then do
[17]			p sei zu b korrespondierender Punkt aus P
[18]				if d(q,p= <= distanz then do
[19]					nachsterNachbar = p
[20]					distanz = d(q,p)
[21]				end if
[22]			end if
[23]			else break // da kein Kandidat näher sein kann
[24]		end for
[25]	end procedure	



*/