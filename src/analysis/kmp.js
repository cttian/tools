					i
T:	a	b	a	b	a	c
P:	a	b	a	b	c
					j
			a	b	a	b	c
					k

T[i-k, i] = P[0, k] = P[j-k, j]

jump[1] = 0;
假设jump[j] = k,则P[0, k] = P[j-k, j];
如果P[k+1] = P[j+1],则jump[j+1] = jump[j] + 1 = k + 1;
如果P[k+1] != P[j+1],则当前已经匹配成功的字符串为P[0,j],因为模式串肯定是从第一个到第j个匹配成功的；
我们就是想找到一个k1，使得P[0, k1] = P[j-k1, j]，我们之前不是已经得到P[0, k] = P[j-k, j],那么k1=k就是我们想要的，
那就是我们接下来可以用P[jump[j]]和P[j+1]来比较。