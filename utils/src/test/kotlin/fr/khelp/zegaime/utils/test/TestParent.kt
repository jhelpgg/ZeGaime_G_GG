package fr.khelp.zegaime.utils.test

open class TestParent(val name : String) : Comparable<TestParent>
{
    override fun compareTo(other : TestParent) : Int =
        this.name.compareTo(other.name)
}