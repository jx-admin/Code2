package java.lang;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

public class String
{
	private static Hashtable stringPool;

	private static Vector ASCIITable;
	
	public String() {

	}
	
	public String(byte[] bytes, int off, int len, String enc) throws UnsupportedEncodingException
	{
		
	}

	public String(char[] bytes, int off, int len)
	{
		
	}
	
	public String(byte[] bytes, int off, int len)
	{
		
	}
	
	public String(byte[] bytes)
	{
		
	}
	
	public String(String value)
	{
		
	}
	
	public String(byte[] bytes, String enc) throws UnsupportedEncodingException
	{
	
	}
	
	public String(StringBuffer buffer)
	{
		
	}
	
	public boolean equalsIgnoreCase(String anotherString)
	{
		return false;
	}
	
	public char[] toCharArray()
	{
		return null;
	}
	
	public boolean endsWith(String suffix)
	{
		return false;
	}
	
	public String concat(String str)
	{
		return null;
	}
	
	public String trim()
	{
		return null;
	}
	
	public byte[] getBytes(String enc) throws UnsupportedEncodingException
	{
		return null;
	}
    
	public byte[] getBytes()
	{
		return null;
	}
	
	public String replace(char oldChar,
            char newChar)
	{
		return null;
		
	}
	
	public int lastIndexOf(int ch)
	{
		return 0;
	}

	public char charAt(int i) {
		return 0;
	}

	public int length() {
		return 0;
	}

	public int indexOf(String s) {
		return 0;
	}

	public int indexOf(String str, int fromIndex) {
		return 0;
	}
	
	public int indexOf(int ch, int fromIndex)
	{
		return 0;
	}

	public boolean startsWith(String s) {
		return false;
	}

	public String substring(int i) {
		return null;
	}

	public String substring(int i, int j) {
		return null;
	}

	public int compareTo(String anotherString) {
		return 0;
	}

	public int indexOf(int ch) {
		return 0;
	}

	public String toLowerCase() {
		return null;
	}

	public String intern() {
		return null;
	}

	public String toUpperCase() {
		return null;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public boolean equals(java.lang.Object o) {
		return super.equals(o);
	}

	public String toString() {
		return super.toString();
	}
	
	public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len)
	{
		return false;
	}
	
	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
	{
	}
	

	
	public static String valueOf(boolean b)
	{
		return null;
	}
	
	public static String valueOf(byte b) {
		return null;
	}
	
	public static String valueOf(short s) {
		return null;
	}
	
	public static String valueOf(char c) {
		return null;
	}
	
	public static String valueOf(char[] data) {
		return null;
	}
	
	public static String valueOf(char[] data, int offset, int count) {
		return null;
	}
	
	public static String valueOf(double d) {
		return null;
	}
	
	public static String valueOf(float f) {
		return null;
	}
	
	public static String valueOf(int i) {
		return null;
	}
	
	public static String valueOf(long l) {
		return null;
	}
	
	public static String valueOf(Object obj) {
		return null;
	}
}
