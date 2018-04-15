/**
 * Copyright 2015-2018 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.internal;

/**
 * @see JsonCodec for rationale
 */
public final class Proto3Codec {
  /**
   * A base 128 varint encodes 7 bits at a time, this checks how many bytes are needed to
   * represent the value.
   *
   * <p>See https://developers.google.com/protocol-buffers/docs/encoding#varints
   *
   * <p>This logic is the same as {@code com.squareup.wire.ProtoWriter.varint32Size} v2.3.0
   * which benchmarked faster than loop variants of the frequently copy/pasted VarInt.varIntSize
   */
  public static int unsignedVarintSize(int value) {
    if ((value & (0xffffffff << 7)) == 0) return 1;
    if ((value & (0xffffffff << 14)) == 0) return 2;
    if ((value & (0xffffffff << 21)) == 0) return 3;
    if ((value & (0xffffffff << 28)) == 0) return 4;
    return 5;
  }

  /** Like {@link #unsignedVarintSize(int)}, except for uint64. */
  static int unsignedVarintSize(long value) {
    if ((value & (0xffffffffffffffffL << 7)) == 0) return 1;
    if ((value & (0xffffffffffffffffL << 14)) == 0) return 2;
    if ((value & (0xffffffffffffffffL << 21)) == 0) return 3;
    if ((value & (0xffffffffffffffffL << 28)) == 0) return 4;
    if ((value & (0xffffffffffffffffL << 35)) == 0) return 5;
    if ((value & (0xffffffffffffffffL << 42)) == 0) return 6;
    if ((value & (0xffffffffffffffffL << 49)) == 0) return 7;
    if ((value & (0xffffffffffffffffL << 56)) == 0) return 8;
    if ((value & (0xffffffffffffffffL << 63)) == 0) return 9;
    return 10;
  }
}
