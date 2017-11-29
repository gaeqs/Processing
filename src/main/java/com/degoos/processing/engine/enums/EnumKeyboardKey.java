package com.degoos.processing.engine.enums;

import java.util.Arrays;
import java.util.Optional;

public enum EnumKeyboardKey {

	/***************************************************************************
	 *                                                                         *
	 * Flags                                                                   *
	 *                                                                         *
	 **************************************************************************/

    /*
     * Key event type.
     */
	PRESS(111),
	RELEASE(112),
	TYPED(113),
	// synthetic

	/*
	 * Key event modifier flags.
	 *
	 * CONTROL/WINDOWS and OPTION/ALT are equal, because they
	 * are mapped to each other on Mac/Windows
	 */
	MODIFIER_NONE(0),
	MODIFIER_SHIFT(1 << 0),
	MODIFIER_FUNCTION(1 << 1),
	MODIFIER_CONTROL(1 << 2),

	MODIFIER_OPTION(1 << 3),
	MODIFIER_ALT(1 << 3),

	// The following should be named Meta perhaps?
	MODIFIER_COMMAND(1 << 4),
	MODIFIER_WINDOWS(1 << 4),

	// Mouse buttons
	MODIFIER_BUTTON_PRIMARY(1 << 5),
	MODIFIER_BUTTON_SECONDARY(1 << 6),
	MODIFIER_BUTTON_MIDDLE(1 << 7),

	/*
	 * Key event key codes.
	 */
	UNDEFINED(0x0),
	// Misc
	ENTER('\n'),
	BACKSPACE('\b'),
	TAB('\t'),
	//    CANCEL             ( 0x03),
	CLEAR(0x0C),
	PAUSE(0x13),
	ESCAPE(0x1B),
	SPACE(0x20),
	DELETE(0x7F),
	PRINTSCREEN(0x9A),
	INSERT(0x9B),
	HELP(0x9C),
	// Modifiers
	SHIFT(0x10),
	CONTROL(0x11),
	ALT(0x12),
	ALT_GRAPH(0xFF7E),
	WINDOWS(0x020C),
	CONTEXT_MENU(0x020D),
	CAPS_LOCK(0x14),
	NUM_LOCK(0x90),
	SCROLL_LOCK(0x91),
	COMMAND(0x0300),
	// Navigation keys
	PAGE_UP(0x21),
	PAGE_DOWN(0x22),
	END(0x23),
	HOME(0x24),
	LEFT(0x25),
	UP(0x26),
	RIGHT(0x27),
	DOWN(0x28),

	// Misc 2
	COMMA(0x2C),
	// ','
	MINUS(0x2D),
	// '-'
	PERIOD(0x2E),
	// '.'
	SLASH(0x2F),
	// '/'
	SEMICOLON(0x3B),
	// '),'
	EQUALS(0x3D),
	// '('
	OPEN_BRACKET(0x5B),
	// '['
	BACK_SLASH(0x5C),
	// '\'
	CLOSE_BRACKET(0x5D),
	// ']'
	MULTIPLY(0x6A),
	// '*'
	ADD(0x6B),
	// '+'
	SEPARATOR(0x6C),
	SUBTRACT(0x6D),
	DECIMAL(0x6E),
	DIVIDE(0x6F),
	AMPERSAND(0x96),
	ASTERISK(0x97),
	DOUBLE_QUOTE(0x98),
	// '"'
	LESS(0x99),
	// '<'
	GREATER(0xa0),
	// '>'
	BRACELEFT(0xa1),
	// '{'
	BRACERIGHT(0xa2),
	// '}'
	BACK_QUOTE(0xC0),
	// '`'
	QUOTE(0xDE),
	// '''
	AT(0x0200),
	// '@'
	COLON(0x0201),
	// ':'
	CIRCUMFLEX(0x0202),
	// '^'
	DOLLAR(0x0203),
	// '$'
	EURO_SIGN(0x0204),
	EXCLAMATION(0x0205),
	// '!'
	INV_EXCLAMATION(0x0206),
	LEFT_PARENTHESIS(0x0207),
	// '('
	NUMBER_SIGN(0x0208),
	// '#'
	PLUS(0x0209),
	// '+'
	RIGHT_PARENTHESIS(0x020A),
	// ')'
	UNDERSCORE(0x020B),
	// '_'
	// Numeric keys
	NUM_0(0x30),
	// '0'
	NUM_1(0x31),
	// '1'
	NUM_2(0x32),
	// '2'
	NUM_3(0x33),
	// '3'
	NUM_4(0x34),
	// '4'
	NUM_5(0x35),
	// '5'
	NUM_6(0x36),
	// '6'
	NUM_7(0x37),
	// '7'
	NUM_8(0x38),
	// '8'
	NUM_9(0x39),
	// '9'
	// Alpha keys
	A(0x41),
	// 'A'
	B(0x42),
	// 'B'
	C(0x43),
	// 'C'
	D(0x44),
	// 'D'
	E(0x45),
	// 'E'
	F(0x46),
	// 'F'
	G(0x47),
	// 'G'
	H(0x48),
	// 'H'
	I(0x49),
	// 'I'
	J(0x4A),
	// 'J'
	K(0x4B),
	// 'K'
	L(0x4C),
	// 'L'
	M(0x4D),
	// 'M'
	N(0x4E),
	// 'N'
	O(0x4F),
	// 'O'
	P(0x50),
	// 'P'
	Q(0x51),
	// 'Q'
	R(0x52),
	// 'R'
	S(0x53),
	// 'S'
	T(0x54),
	// 'T'
	U(0x55),
	// 'U'
	V(0x56),
	// 'V'
	W(0x57),
	// 'W'
	X(0x58),
	// 'X'
	Y(0x59),
	// 'Y'
	Z(0x5A),
	// 'Z'
	// Numpad keys
	NUMPAD0(0x60),
	NUMPAD1(0x61),
	NUMPAD2(0x62),
	NUMPAD3(0x63),
	NUMPAD4(0x64),
	NUMPAD5(0x65),
	NUMPAD6(0x66),
	NUMPAD7(0x67),
	NUMPAD8(0x68),
	NUMPAD9(0x69),
	// Function keys
	F1(0x70),
	F2(0x71),
	F3(0x72),
	F4(0x73),
	F5(0x74),
	F6(0x75),
	F7(0x76),
	F8(0x77),
	F9(0x78),
	F10(0x79),
	F11(0x7A),
	F12(0x7B),
	F13(0xF000),
	F14(0xF001),
	F15(0xF002),
	F16(0xF003),
	F17(0xF004),
	F18(0xF005),
	F19(0xF006),
	F20(0xF007),
	F21(0xF008),
	F22(0xF009),
	F23(0xF00A),
	F24(0xF00B),

	DEAD_GRAVE(0x80),
	DEAD_ACUTE(0x81),
	DEAD_CIRCUMFLEX(0x82),
	DEAD_TILDE(0x83),
	DEAD_MACRON(0x84),
	DEAD_BREVE(0x85),
	DEAD_ABOVEDOT(0x86),
	DEAD_DIAERESIS(0x87),
	DEAD_ABOVERING(0x88),
	DEAD_DOUBLEACUTE(0x89),
	DEAD_CARON(0x8a),
	DEAD_CEDILLA(0x8b),
	DEAD_OGONEK(0x8c),
	DEAD_IOTA(0x8d),
	DEAD_VOICED_SOUND(0x8e),
	DEAD_SEMIVOICED_SOUND(0x8f);

	private int id;

	EnumKeyboardKey(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static Optional<EnumKeyboardKey> getById(int id) {
		return Arrays.stream(values()).filter(target -> target.getId() == id).findAny();
	}

}
