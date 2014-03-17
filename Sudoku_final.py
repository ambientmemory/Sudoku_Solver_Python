import numpy as n
def slv(ans):
    global d,fld,mn,oc,ol
    if d==1:return 0
    if ocal()==0:
        lol=ol
        loc=oc
        for i in range(loc):
            ans[mn[0]][mn[1]]=lol[i]
            fld+=1
            if fld==81:
                d=1
                return 0
            lmn=mn
            lbd=ans
            if slv(lbd)==-1:
                fld-=1 
                ans[lmn[0]][lmn[1]]=0
                mn=lmn
                ans=lbd
            else:return 0
    return -1
def ocal():
    global ans,oc,ol,mn
    arc=n.full((9,9),10)
    for i in range(9):
        for j in range(9):
            if ans[i][j]>0:
                continue
            arc[i][j]=0
            for x in range(1,10):
                if (not x in ans[i] and not x in ans[:,j]and sq(x,i,j)):
                    arc[i][j]+=1
            if arc[i][j]==0:
                return -1
    mn=n.unravel_index(arc.argmin(),arc.shape)
    k=0
    for x in range(1,10):
        if (not x in ans[mn[0]] and not x in ans[:,mn[1]]and sq(x,mn[0],mn[1])):
            ol[k]=x
            k+=1
    oc=k+1
    return 0
def sq(a,x,y):
    global ans
    for p in range(3):
        for q in range(3):
            if ans[(x/3)*3+p][(y/3)*3+q]==a:
                return 0
    return 1
v=n.fromfile("input.txt", dtype=int, count=1, sep=' ')
h=n.loadtxt("input.txt", dtype=int, skiprows=1)
st=0
for i in range(v):
    d=0 
    ol=n.zeros(9)
    oc=10
    mn=[10,10]
    ans=h[st:st+9,0:9]
    st+=9
    fld=n.count_nonzero(ans)
    if slv(ans)==-1:print"No solution..."
    else: print ans
